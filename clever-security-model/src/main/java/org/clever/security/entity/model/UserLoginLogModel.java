package org.clever.security.entity.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 作者： lzw<br/>
 * 创建时间：2018-10-21 22:11 <br/>
 */
@Data
public class UserLoginLogModel implements Serializable {
    /*


TODO
select
  a.*,
  b.id as user_id,
  b.user_type,
  b.telephone,
  b.email,
  b.expired_time,
  b.locked,
  b.enabled,
  b.description
from user_login_log a
  left join user b on (a.username = b.username)
where a.username = ''
  and a.sys_name = ''
  and a.login_time >= ''
  and a.login_time <= ''
  and a.login_state=''
  and b.telephone = ''
  and b.email = ''
order by a.update_at DESC, a.login_time DESC





    *
    * */
}
