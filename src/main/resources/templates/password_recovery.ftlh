<#import "partials/main.ftlh" as main />

<@main.renderWith title="Password Recovery"
scripts=["/js/navbar.js","/js/dropdown_script.js","/js/nested_dropdown.js","/js/main_page.js"]
styles=["https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.3/font/bootstrap-icons.css",
"/css/navbar.css", "/css/nested_dropdown.css","/css/main_page.css","/css/dropdown_style.css"]>
    <#assign security=JspTaglibs["http://www.springframework.org/security/tags"]/>

    <div>
        <h2 style="margin: 50px 0 20px 0;">Пожалуйста введите повторно вашу электронную почту</h2>
        <form action="/password-recovery" method="post">
            <#if _csrf??>
                <input type="hidden" name="${(_csrf.parameterName)!'csrf'}" value="${(_csrf.token)!'--no-token--'}"/>
            </#if>

            <div class="form-group recovery-password">
                <label for="email">Адрес почты</label>
                <input type="email" name="username" class="form-control" id="email" placeholder="Ваш адрес электронной почты"
                       value="${(dto.email)!''}" autofocus>
            </div>
            <div>
                <button type="submit" style="margin-top: 20px" class="btn btn-primary">Продолжить</button>
            </div>
        </form>
    </div>
</@main.renderWith>
