
/**
 * nobone-sync 文件同步配置
 * 参考：https://github.com/ysmood/nobone-sync
 */

module.exports = {
    //本地需要同步到远端的文件
    localDir: 'WEB-INF/views',
    //远程需要同步的文件夹
    remoteDir: '/usr/local/tomcat/tomcat-7.0.67/webapps/campussay/WEB-INF/views',
    // It decides the root accessible path.
    rootAllowed: '/',
    //远端服务器地址
    host: '103.37.161.234',
    //远端nobone-sync服务器启动的监听端口
    port: 8345,
    pattern: '**',
    pollingInterval: 500,
    // If it is set, transfer data will be encrypted with the algorithm.
    password: null,
    algorithm: 'aes128'
} 
