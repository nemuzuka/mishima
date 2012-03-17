<%@page pageEncoding="UTF-8" isELIgnored="false"%>
<!-- Navbar
    ================================================== -->
<div class="navbar navbar-fixed-top">
  <div class="navbar-inner">
    <div class="container-fluid">
      
      <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </a>
      
      <a class="brand" href="http://twitter.github.com/bootstrap/index.html">Mishima</a>
      <div class="nav-collapse" style="float: right;">
        <ul class="nav">
          <li style="color:#FFF;height: 40px;margin:auto;line-height: 40px;" id="selected_project_name">
            【プロジェクト名】
          </li>
          <li class="divider-vertical"></li>
          <li class="" style="line-height: 40px;">
            <select id="targetProjects">
              <option>--プロジェクトを選択--</option>
              <option>XXXプロジェクト</option>
              <option>YYYプロジェクト</option>
            </select>
          </li>
          <li class="">
            <a href="#" id="personal_settings">個人設定</a>
          </li>
          <li class="">
            <a href="#" id="logout">logout</a>
          </li>
        </ul>
      </div>
    </div>
  </div>
  <!-- 各機能に対するメニューバー(2段目) -->
  <div class="navbar-inner">
    <div class="container">
      <div class="nav-collapse">
        <ul class="nav">
          <li class="active">
            <a href="#">ダッシュボード</a>
          </li>
          <li class="">
            <a href="#">チケット</a>
          </li>
          <li class="divider-vertical"></li>
          <li class="">
            <a href="#">プロジェクト設定</a>
          </li>
          <li class="">
            <a href="#">メンバー管理</a>
          </li>
          <li class="">
            <a href="#">プロジェクト管理</a>
          </li>
        </ul>
      </div>
    </div>
  </div>
</div>
