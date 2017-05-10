<#--
 - 此文件由 mt-fe 模块维护，修改请绕行 mt-fe
 -
 - 请注意, 该文件只在线下生效，所有配置对线上不起作用
 - 要使用自己的开发环境, 请在header.inc文件中配置cos_baseUri即可, 避免修改此文件，导致重启被覆盖
 -->

<#if !cos_yuiVersion??  ><#assign cos_yuiVersion = '3.13.0' ></#if>
<#if !cos_static??      ><#assign cos_static = "static" ></#if>
<#if !cos_siteGroupKey??><#assign cos_siteGroupKey = 'mt' + cos_siteKey ></#if>
<#if !cos_baseUri ??    ><#assign cos_baseUri = 'http://develop.cs0.meituan.net/'></#if>
<#if !cos_comboBase ??  ><#assign cos_comboBase = 'http://develop.jc.meituan.net/combo/?f='></#if>

<script>
var M = window.M || {};
M.yuiVersion = '${cos_yuiVersion}';
M.baseUri = '${cos_baseUri}';
M.comboBase = '${cos_comboBase}';
M.combine = false;
M.debug = true;
M.cosGroups = [
    { name: 'cos.ui', prefix: 'ui-' },
    {
        name: '${cos_siteGroupKey}',
        prefix: '${cos_siteKey}-',
        path: 'http://' + location.host + '${base}/${cos_static}/',
        combine: false
    }
];
M.site = '${cos_siteKey}';
</script>

<#if cos_useUI ?? && cos_useUI == true>
<link href="${cos_baseUri}cos.ui/cos-ui.css" rel="stylesheet">
</#if>

<#if cos_siteKey ?? && cos_siteKey == "oa">
<script src="${cos_comboBase}yui/${cos_yuiVersion}/build/yui/yui.js;yui-config-mt${cos_siteKey}.js"></script>
<#else>
<script src="${cos_comboBase}mt-yui-core.v${cos_yuiVersion}.js;yui-config.js"></script>
</#if>
