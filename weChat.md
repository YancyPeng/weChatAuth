###一、微信开放平台 第三方接入 授权：

####1.微信服务器会向《授权事件接受URL》每隔10分钟定时推送component_verify_ticket，在收到ticket推送后需要进行解密获取 component_verify_ticket。

####2.根据 component_verify_ticket，component_appid，component_appsecret 来获得componet_access_token，该令牌的存在有效期为2个小时，且令牌的调用不是无限制的（2000次）

  需要一个定时任务，如1小时50分钟请求一次，保存进数据库
	
	http请求方式: POST（请使用https协议） https://api.weixin.qq.com/cgi-bin/component/api_component_token

	POST数据示例:

	{
	"component_appid":"appid_value" ,
	"component_appsecret": "appsecret_value", 
	"component_verify_ticket": "ticket_value" 
	}


####3.根据componet_access_token来获得 预授权码 pre_auth_code  有效期10分钟

	http请求方式: POST（请使用https协议）

	https://api.weixin.qq.com/cgi-bin/component/api_create_preauthcode?component_access_token=xxx

	POST数据示例:

	{
	"component_appid":"appid_value" 
	}


####4.引导进入授权页面，参数为https://mp.weixin.qq.com/cgi-bin/componentloginpage?component_appid=xxxx&pre_auth_code=xxxxx&redirect_uri=xxxx，授权成功后会回调uri
  会将用户的授权码authorization_code返回 该授权码的有效期为10分钟

	前端界面（按钮）在后端redirect重定向：https://mp.weixin.qq.com/cgi-bin/componentloginpage?component_appid=xxxx&pre_auth_code=xxxxx&redirect_uri=xxxx（这个uri写成授权事件URl）


####5.回调《授权事件接受URL》，发送一个授权相关通知，通过infoType来区分是取消授权、更新授权后者授权成功通知。

  1.unauthorized 取消授权

  2.updateauthorized 更新授权

  3.authorized 授权成功 // 新授权后调用查询素材列表接口

--------------



###二、根据授权码 获得微信公众号的基本信息

####1.利用授权码authorization_code获取公众号的授权信息： 

  得到公众号appid： authorizer_appid ，公众号调用接口凭据authorizer_access_token，（这里的token还是两个小时刷新一次，所以可以和componet_access_token放在一个实体类中）有效期2个小时

  刷新令牌authorizer_refresh_token
	
	http请求方式: POST（请使用https协议）

	https://api.weixin.qq.com/cgi-bin/component/api_query_auth?component_access_token=xxxx

	POST数据示例:

	{
	"component_appid":"appid_value" ,
	"authorization_code": "auth_code_value"
	}

####2.如果authorizer_access_token过期了，需要使用刷新令牌authorizer_refresh_token重新获取 两个令牌的有效期都是2个小时

  http请求方式: POST（请使用https协议）

  https:// api.weixin.qq.com /cgi-bin/component/api_authorizer_token?component_access_token=xxxxx

  POST数据示例:

  {
  "component_appid":"appid_value",
  "authorizer_appid":"auth_appid_value",
  "authorizer_refresh_token":"refresh_token_value",
  }

####3.最后拿到 authorizer_appid 去获取公众号的信息：

	http请求方式: POST（请使用https协议）
	https://api.weixin.qq.com/cgi-bin/component/api_get_authorizer_info?component_access_token=xxxx

	POST数据示例:

	{
	"component_appid":"appid_value" ,
	"authorizer_appid": "auth_appid_value" 
	}



----------


###二、根据授权码authorizer_access_token去调用公众号相关API，调用方式和公众号使用自身API方式一样。（只是将access_token参数替换成authorizer_access_token）

####1.素材管理

  创建：只支持新增永久图文素材（需要提供图片的素材media_id，新增图片素材接口，同时将该图片上传至本地文件服务器，返回url插入表中），在创建自动回复时若调用了新增图片素材接口返回的media_id需要
  保存在本地，在消息表中保存），创建完成会返回一个media_id

  需要新建一个素材实体类

  删除：

  查询：由于微信的图片url是防盗的，所以查询不调用微信API，直接在本地获得图片，查询时查询本地的表(如何获得微信已存在的素材？通过获取素材列表接口获得所有的media
  _id)，其他创建修改删除需要同时修改本地和微信

  修改：

####2.菜单管理

  创建：在创建菜单时，当菜单的相应动作类型为click时，在回复表中增加一条数据，返回一个key（这个可以对应为回复表中的回复id），将key发给微信端，为view时不做处理直接发送给微信端

  删除：删除时会默认删除所有的自定义菜单，如果想删除单个菜单，应该去调用修改接口，即创建接口

  查询：

  修改：由于微信公众平台没有提供修改接口，只能再调一次创建接口

####3.自动回复
  
  1.关注后自动回复，一个公众号只有一个

  * 创建自动回复：

      >如果自动回复类型为图片，需要先上传到微信，然后返回一个media_id。记录到本地自动回复表中

        http请求方式: POST，https协议

        https://api.weixin.qq.com/cgi-bin/media/uploadimg?access_token=ACCESS_TOKEN
        
      ```
          {
            "access_token" : "",
            "media" : "MEDIA_ID"
          }
      ```

      （2）文本

      （3）图文


      需要在本地新建一个订阅回复的表，在接收到回复事件时返回给用户

      http请求方式: POST

      https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN
      各消息类型所需的JSON数据包如下：

      发送图文消息
    ```
      {
          "touser":"OPENID",  // 为了识别用户，每个用户针对每个公众号会产生一个安全的OpenId，在接受用户发送的消息及关注的时候都可以获取
          "msgtype":"mpnews",
          "mpnews":
          {
               "media_id":"MEDIA_ID"
          }
      }
    ```
  * 输入关键字后自动回复：  需要在本地建一个关键字表，接受用户输入后 查询 是否有相关回复内容。


    >创建自动回复：

      （1）如果自动回复类型为图片，需要先上传到微信服务器（因为在回复图片的时候需要传media_id），然后返回一个media_id。记录到本地自动回复表中

        http请求方式: POST，https协议

        https://api.weixin.qq.com/cgi-bin/media/uploadimg?access_token=ACCESS_TOKEN

       ```
          {
            "access_token" : "",
            "media" : "MEDIA_ID"
          }
        ```

      （2）文本

      （3）图文

    2>进行回复：

      >http请求方式: POST

      https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN
      各消息类型所需的JSON数据包如下：

      发送文本消息

    ```
      {
          "touser":"OPENID",  // 为了识别用户，每个用户针对每个公众号会产生一个安全的OpenId，在接受用户发送的消息及关注的时候都可以获取
          "msgtype":"text",
          "text":
          {
               "content":"Hello World"
          }
      }
    ```

------------
###三、数据库设计

####1.公众号基础信息表 


####2.授权码相关信息表


####3.图文素材信息表


####4.关键字表（订阅回复 只需将关键字设置“订阅”）


####5.回复表


####6.订阅回复表

* 注意：

  >   在实际部署时，需要更换JAVA安全包相关的内容，否则将出现秘钥长度不够的异常，
      需要替换的文件包括JAVA_HOME/jre/lib/security/local_policy.jar和  JAVA_HOME/jre/lib/security/US_export_policy.jar这两个文件。









