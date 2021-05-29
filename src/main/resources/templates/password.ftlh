<#-- @ftlvariable name="password" type="java.lang.String" -->

<#import "email.ftlh" as template>
<@template.email "Используйте этот пароль для входа.">
    <p>Здравствуйте, уважаемый пользователь!</p>
    <p>После активации аккаунта, используйте следующий пароль для входа: <b>${password}</b></p>
</@template.email>
