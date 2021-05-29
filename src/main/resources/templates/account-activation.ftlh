<#-- @ftlvariable name="activationLink" type="java.lang.String" -->
<#-- @ftlvariable name="timeLimitInMinutes" type="java.lang.Number" -->
<!--suppress HtmlDeprecatedAttribute -->
<#import "email.ftlh" as template>
<@template.email "Пожалуйста, подтвердите электронную почту.">
    <p>Здравствуйте, уважаемый пользователь!</p>
    <p>Ваша учётная запись успешно создана. Пожалуйста, нажмите на кнопку ниже, чтобы активировать.</p>
    <table role="presentation" border="0" cellpadding="0" cellspacing="0"
           class="btn btn-primary">
        <tbody>
        <tr>
            <td align="left">
                <table role="presentation" border="0" cellpadding="0"
                       cellspacing="0">
                    <tbody>
                    <tr>
                        <td>
                            <a href="${activationLink}" target="_blank">Активировать</a>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </td>
        </tr>
        </tbody>
    </table>
    <p>Кнопка активации будет работать ещё ${timeLimitInMinutes} минут.</p>
</@template.email>
