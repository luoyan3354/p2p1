<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css"/>
<script language="javascript" src="${pageContext.request.contextPath}/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jQuery.md5.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/register.js"></script>
  <script type="text/javascript">

    //手机号失去光标时触发的事件
    function checkPhone(){

      //获取用户的手机号
      var phone = $.trim($("#phone").val());
      var flag = true;

      if("" == phone){
        showError("phone", "请输入手机号码");
        return false;
      }else if(!/^1[1-9]\d{9}$/.test(phone)){
        showError("phone", "请输入正确的手机号码");
        return false;
      }else{
        //alert("发送ajax请求");
        $.ajax({
          url:"loan/checkPhone",
          type:"post",
          async:false,
          data:"phone="+phone,
          success:function (jsonObject) {
            if(jsonObject.errorMessage == "OK"){
              showSuccess("phone");
              flag = true;
            }else{
              showError("phone", jsonObject.errorMessage);
              flag = false;
            }
          },
          error:function () {
            showError("phone", "系统繁忙中，请稍后重试...");
            flag = false;

          }
        });
      }

      return flag;

    }

    //密码失去光标时触发的事件
    function checkLoginPassword() {
      //获取用户输入的登录密码
      var loginPassword = $.trim($("#loginPassword").val());
      var replayLoginPassword = $.trim($("#replayLoginPassword").val());

      if("" == loginPassword){
        showError("loginPassword", "请输入登录密码");
        return false;
      }else if(!/^[0-9a-zA-Z]+$/.test(loginPassword)){
        showError("loginPassword", "密码只支持数字和大小写英文字母");
        return false;
      }else if(!/^(([a-zA-Z]+[0-9]+)|([0-9]+[a-zA-Z]+))[a-zA-Z0-9]*/.test(loginPassword)){
        showError("loginPassword", "密码应同时包含英文和数字");
        return false;
      }else if(loginPassword.length < 6 || loginPassword.length > 20){
        showError("loginPassword", "密码长度应为6-20位");
        return false;
      }else{
        showSuccess("loginPassword");
      }

      //友好提示用户不要忘记输入再次登录密码
      if(replayLoginPassword != loginPassword){
        showError("replayLoginPassword", "两次密码输入不一致");
      }

      return true;

    }

    //确认密码失去光标时触发的事件
    function checkLoginPasswordEqu() {
      var loginPassword = $.trim($("#loginPassword").val());
      var replayLoginPassword = $.trim($("#replayLoginPassword").val());

      if("" == loginPassword){
        showError("loginPassword", "请输入登录密码");
        return false;
      }else if("" == replayLoginPassword){
        showError("replayLoginPassword", "请输入确认登录密码");
        return false;
      }else if(replayLoginPassword != loginPassword){
        showError("replayloginPassword", "确认密码两次输入密码不一致");
        return false;
      }else{
        showSuccess("replayLoginPassword");
      }

      return true;

    }

    //验证图形验证码
    function checkCaptcha() {
      var captcha = $.trim($("#captcha").val());
      var flag = true;

      if("" == captcha){
        showError("captcha", "请输入图形验证码");
        return false;
      }else{
        $.ajax({
          url:"loan/checkCaptcha",
          type:"post",
          async:false,
          data:"captcha=" + captcha,
          success:function (jsonObject) {
            if(jsonObject.errorMessage == "OK"){
              showSuccess("captcha");
              flag = true;
            }else{
              showError("captcha", jsonObject.errorMessage);
              flag = false;
            }
          },
          error:function () {
            showError("captcha", "系统繁忙，请稍后重试...");
            flag = false;
          }
        });
      }

      return flag;

    }

    //注册信息全部输入完成之后点击注册
    function register() {

      //获取用户注册的参数
      var phone = $.trim($("#phone").val());
      var loginPassword = $.trim($("#loginPassword").val());
      var replayLoginPassword = $.trim($("#replayLoginPassword").val());

      if(checkPhone() && checkLoginPassword() && checkLoginPasswordEqu() && checkCaptcha()){
        $.ajax({
          url:"loan/register",
          type:"post",
          data:{
            "phone":phone,
            "loginPassword":$.md5(loginPassword),
            "replayLoginPassword":$.md5(replayLoginPassword)
          },
          success:function (jsonObject) {
            if(jsonObject.errorMessage == "OK"){
              //跳转至实名认证页面
              window.location.href = "realName.jsp";
            }else{
              showError("captcha",jsonObject.errorMessage);
            }
          },
          error:function () {
            showError("captcha", "系统繁忙，请稍后重试");

          }
        })
      }

    }

    //错误提示
    function showError(id,msg) {
      $("#"+id+"Ok").hide();
      $("#"+id+"Err").html("<i></i><p>"+msg+"</p>");
      $("#"+id+"Err").show();
      $("#"+id).addClass("input-red");
    }
    //错误隐藏
    function hideError(id) {
      $("#"+id+"Err").hide();
      $("#"+id+"Err").html("");
      $("#"+id).removeClass("input-red");
    }
    //显示成功
    function showSuccess(id) {
      $("#"+id+"Err").hide();
      $("#"+id+"Err").html("");
      $("#"+id+"Ok").show();
      $("#"+id).removeClass("input-red");
    }

    //注册协议确认
    $(function() {
      $("#agree").click(function(){
        var ischeck = document.getElementById("agree").checked;
        if (ischeck) {
          $("#btnRegist").attr("disabled", false);
          $("#btnRegist").removeClass("fail");
        } else {
          $("#btnRegist").attr("disabled","disabled");
          $("#btnRegist").addClass("fail");
        }
      });
    });

    //打开注册协议弹层
    function alertBox(maskid,bosid){
      $("#"+maskid).show();
      $("#"+bosid).show();
    }
    //关闭注册协议弹层
    function closeBox(maskid,bosid){
      $("#"+maskid).hide();
      $("#"+bosid).hide();
    }
  </script>
<title>注册动力金融网-动力金融网,专业的互联网金融信息服务平台</title>
</head>

<body>
<div id="header">
<jsp:include page="commons/header.jsp"/>
</div>

<div class="login-body regBody">
  <div class="mainBox">
    <div class="homeWap">
    
      <div class="reg-step">
      <ul class="clearfix">
        <li>注册</li>
        <li class="none">实名认证</li>
        <li class="last">完成</li>
      </ul>
      </div>
      
      <div class="login-cnt reg-body clearfix">
        <div class="fl">
            <div class="reg-link">已有帐号？<a href="${pageContext.request.contextPath}/login.jsp">登录</a></div>
            <div class="login-form reg-form">
              <h2>用户注册</h2>
              
              <div class="login-box clearfix" style="z-index:100;">
                <label>手机号</label>
                <input type="text" id="phone" name="phone" class="input_text" maxlength="11"
                       placeholder="请输入11位手机号码" autocomplete="off" onblur="checkPhone()"/>
                <span id="phoneOk" class="ok" style="display:none;"></span>
                <div id="phoneErr" class="form-hint" style="display:none;"></div>
              </div>
              
              <div class="login-box clearfix " style="z-index:90;">
                <label>登录密码</label>
                <input type="password" id="loginPassword" name="loginPassword" class="input_text" maxlength="20"
                       placeholder="请输入6-20位英文和数字混合密码" autocomplete="off" onblur="checkLoginPassword();"/>
                <span id="loginPasswordOk" class="ok" style="display:none;"></span>
                <div id="loginPasswordErr" class="form-hint" style="display:none;z-index:90;"></div>
              </div>
              
              <div class="login-box clearfix" style="z-index:80;">
                <label>确认密码</label>
                <input type="password" id="replayLoginPassword" name="replayLoginPassword" class="input_text" maxlength="21" placeholder="请再次输入登录密码" onblur="checkLoginPasswordEqu()"/>
                <span id="replayLoginPasswordOk" class="ok" style="display:none;"></span>
                <div id="replayLoginPasswordErr" class="form-hint" style="display:none;"></div>
              </div>
              
              <div class="login-box clearfix" style="z-index:60;">
                <label>图形验证码</label>
                <div class="yzm-box">
                  <input id="captcha" name="captcha" type="text" class="yzm" maxlength="10" placeholder="点击右侧图片可刷新" onblur="checkCaptcha();"/>
                  <a style='cursor:pointer;'><img src="${pageContext.request.contextPath}/jcaptcha/captcha?d="+new Date().getTime() onclick="this.src='${pageContext.request.contextPath}/jcaptcha/captcha?d='+new Date().getTime()"/></a>
                </div>
                <span id="captchaOk" class="ok" style="display:none;"></span>
                <div id="captchaErr" class="form-hint" style="display:none;"></div>
              </div>
              
              <div class="remember" style="z-index:60;">
                <span class="on">
                <input id="agree" name="agree" type="checkbox"/>我已阅读并同意<a href="javascript:alertBox('mask','agreement');"><font style="color: red;">《动力金融网&trade;注册服务协议》</font></a>
                </span>
              </div>
              
              <div class="bn-login">
                <button class="fail" id="btnRegist" onclick="javascript:register();" disabled>注&nbsp;&nbsp;册</button>
              </div>
              
          </div>
        </div>
        <div class="reg-right">
          <div class="reg-txt clearfix">
            <p>
            	万名用户知心托付<br/>
              	千万级技术研发投入<br/>
            </p>
            <p>
            	12.7%历史年化收益<br/>
              	亿级注册资本平台
            </p>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<!--页脚start-->
<jsp:include page="commons/footer.jsp"/>
<!--页脚end-->

<!-- 注册协议模板start -->
<span id="includeAgreement">
	<jsp:include page="commons/registerAagreement.jsp"/>
</span>
<!-- 注册协议模板end -->

<!--遮罩层start-->
<div id="mask" class="dialog-overlay" style="display:none;"></div>
<!--遮罩层end-->

</body>
</html>