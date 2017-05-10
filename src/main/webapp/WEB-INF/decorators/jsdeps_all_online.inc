<#--
 - 此文件由 mt-fe 模块维护，修改请绕行 mt-fe
 -->

<#assign IS_ONLINE_FILE  = true >

<#assign
    cos_domainHost = 'meituan.net'
    cos_comboBase = 'http://jc.${cos_domainHost}/combo/?f='
    cos_baseUri = 'http://cs0.${cos_domainHost}/'
    cos_debug = false
    cos_min = '-min'
>

<#if !cos_siteGroupKey??>
    <#assign cos_siteGroupKey = '{SITE_GROUP_KEY}' >
</#if>

<#if ! cos_yuiVersion?? >
    <#assign cos_yuiVersion = '3.10.3' >
</#if>

<#if RequestParameters?? && RequestParameters.cos_debug??>
    <#assign cos_debug = RequestParameters.cos_debug != 'false' >
</#if>
<#if RequestParameters?? && RequestParameters.cos_baseUri??>
    <#assign cos_baseUri = RequestParameters.cos_baseUri >
</#if>

<#if cos_debug>
    <#assign cos_min = '' >
</#if>

<script>
var M = window.M || {};
M.yuiVersion = '${cos_yuiVersion}';
M.baseUri = '${cos_baseUri}';
M.comboBase = '${cos_comboBase}';
M.debug = ${cos_debug?string('true', 'false')};
M.combine = true;
M.site = '${cos_siteKey}'.replace('mt', '');

// cos_siteGroupKey的值由系统发布时确定
// cos_siteKey 由系统header.inc 里面指定, 作为模块加载的前缀
M.cosGroups = [
    {name: 'cos.ui', prefix: 'ui-'},
    {name: '${cos_siteGroupKey}', prefix: '${cos_siteKey}-'}
];
</script>

<#if cos_useUI ?? && cos_useUI == true>
<link href="${cos_baseUri}cos.ui/cos-ui${cos_min}.v{UI_VERSION}.css" rel="stylesheet">
</#if>

<#if cos_siteKey == "mtoa">
<script src="${cos_baseUri}yui/${cos_yuiVersion}/build/yui/yui${cos_min}.js"></script>
<#else>
<script src="${cos_comboBase}mt-yui-core${cos_min}.v${cos_yuiVersion}.js"></script>
</#if>

<#--
//前端小流量上线, 非小流量指定yui-config的版本号支持
-->
<#if ! cos_isSmallFlow?? || ! cos_yuiConfigVersion?? || cos_isSmallFlow == true >
<script src="${cos_baseUri}yui-config-{SITE_GROUP_KEY}${cos_min}.v{YUI_CONFIG_VERSION}.js"></script>
<#else>
<script src="${cos_baseUri}yui-config-{SITE_GROUP_KEY}${cos_min}.v${cos_yuiConfigVersion}.js"></script>
</#if>

