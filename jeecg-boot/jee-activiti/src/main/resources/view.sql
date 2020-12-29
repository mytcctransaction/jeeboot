CREATE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `act_id_group` AS select `r`.`id` AS `ID_`,NULL AS `REV_`,`r`.`depart_name` AS `NAME_`,'assignment' AS `TYPE_` from `sys_depart` `r`

CREATE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `act_id_membership` AS select (select `u`.`username` from `sys_user` `u` where (`u`.`id` = `ur`.`user_id`)) AS `USER_ID_`,(select `r`.`id` from `sys_depart` `r` where (`r`.`id` = `ur`.`dep_id`)) AS `GROUP_ID_` from `sys_user_depart` `ur`


CREATE ALGORITHM=UNDEFINED  SQL SECURITY DEFINER VIEW `act_id_user` AS select `u`.`id` AS `ID_`,0 AS `REV_`,`u`.`username` AS `FIRST_`,'' AS `LAST_`,`u`.`email` AS `EMAIL_`,`u`.`password` AS `PWD_`,'' AS `PICTURE_ID_` from `sys_user` `u`