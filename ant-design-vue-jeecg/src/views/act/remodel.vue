<template>
  <div class="mod-remodel">
    <el-form :inline="true" :model="searchForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-input v-model="searchForm.key" placeholder="关键字" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
        <el-button  type="primary" @click="addOrUpdateHandle()">新增</el-button>
        <el-button  type="danger" @click="deleteHandle()"
                   :disabled="dataListSelections.length <= 0">批量删除
        </el-button>
      </el-form-item>
    </el-form>
    <el-table
      :data="dataList"
      border
      @selection-change="selectionChangeHandle"
      style="width: 100%;">
      <el-table-column
        type="selection"
        header-align="center"
        align="center"
        width="50">
      </el-table-column>
      <el-table-column
        prop="name"
        header-align="center"
        align="center"
        label="模型名称">
      </el-table-column>
      <el-table-column
        prop="key"
        header-align="center"
        align="center"
        label="关键字">
      </el-table-column>
      <el-table-column
        width="250px"
        prop="createTime"
        header-align="center"
        align="center"
        label="创建时间">
      </el-table-column>
      <el-table-column
        width="250px"
        prop="lastUpdateTime"
        header-align="center"
        align="center"
        label="最后修改时间">
      </el-table-column>
      <el-table-column
        prop="version"
        header-align="center"
        align="center"
        label="版本">
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="200"
        label="操作">
        <template slot-scope="scope">
          <el-button  type="text" size="small"
                     @click="editorHandle(scope.row.id)">编辑
          </el-button>
          <el-button  type="text" size="small" @click="deployHandle(scope.row.id)">
            部署
          </el-button>
          <el-button  type="text" size="small"
                     @click="exportHandle(scope.row.id)">导出
          </el-button>
          <el-button type="text" size="small" @click="deleteHandle(scope.row.id)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="sizeChangeHandle"
      @current-change="currentChangeHandle"
      :current-page="pageIndex"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="pageSize"
      :total="totalPage"
      layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
  </div>
</template>

<script>
  import AddOrUpdate from './remodel-add-or-update'

  export default {
    data () {
      return {
        searchForm: {
          key: ''
        },
        dataList: [],
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0,
        dataListSelections: [],
        addOrUpdateVisible: false
      }
    },
    components: {
      AddOrUpdate
    },
    activated () {
      this.getDataList()
    },
    methods: {
      // 获取数据列表
      getDataList () {
        this.$http({
          url: '/act/remodel/list',
          method: 'get',
          params: {
            'page': this.pageIndex,
            'limit': this.pageSize,
            'key': this.searchForm.key
          }
        }).then(data => {
          console.log(data)
          if (data && data.code === 0) {
            console.log('1111')
            this.dataList = data.page.records
            console.log('22:'+this.dataList)
            this.totalPage = data.page.total
          } else {
            this.dataList = []
            this.totalPage = 0
          }
        })
      },
      // 每页数
      sizeChangeHandle (val) {
        this.pageSize = val
        this.pageIndex = 1
        this.getDataList()
      },
      // 当前页
      currentChangeHandle (val) {
        this.pageIndex = val
        this.getDataList()
      },
      // 多选
      selectionChangeHandle (val) {
        this.dataListSelections = val
      },
      // 新增 / 修改
      addOrUpdateHandle (id) {
        this.addOrUpdateVisible = true
        this.$nextTick(() => {
          this.$refs.addOrUpdate.init(id)
        })
      },
      // 删除
      deleteHandle (id) {
        let ids = id ? [id] : this.dataListSelections.map(item => {
          return item.id
        })

        this.$confirm({
          content: `确定对[id=${ids.join(',')}]进行删除操作？`,
          onOk: () => {
            this.$http({
              url: '/act/remodel/delete',
              method: 'post',
              data: ids
            }).then(data => {
              if (data && data.code === 0) {

                this.$message.success('操作成功')
                this.getDataList()
              }
            })
          }
        })

      },
      // 部署
      deployHandle (id) {
        this.$http({
          url: '/act/remodel/deploy?id=' + id,
          method: 'post'
        }).then(data => {
          if (data && data.code === 0) {
            this.$message.success(data.msg)

            this.getDataList()
          }else {
            this.$message.error("部署失败，请检测流程定义文件")
            this.getDataList()
          }
        })
      },
      // 导出xml
      exportHandle (id) {
        let url = window._CONFIG['domianURL']+`/act/remodel/export?id=${id}`
        window.open(url)
      },
      // 编辑
      editorHandle (id) {

        let url =  window._CONFIG['domianURL']+`/modeler.html?modelId=${id}`
        console.log("url:"+url);
        window.open(url)
      }
    }
  }
</script>
