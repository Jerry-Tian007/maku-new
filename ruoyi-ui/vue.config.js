// @ts-nocheck
'use strict'
const path = require('path')

function resolve (dir) {
  return path.join(__dirname, dir)
}

const CompressionPlugin = require('compression-webpack-plugin')

const name = process.env.VUE_APP_TITLE || 'NY-Zodiac' // 网页标题

const port = process.env.port || process.env.npm_config_port || 9801 // 端口

module.exports = {  publicPath: process.env.NODE_ENV === "production" ? "/" : "/",
  outputDir: 'dist',
  assetsDir: 'static',
  lintOnSave: process.env.NODE_ENV === 'development',
  productionSourceMap: false,
  devServer: {
    host: '0.0.0.0',
    port: port,
    open: true,
    proxy: {
      [process.env.VUE_APP_BASE_API]: {
        // target: `http://10.115.21.37:3333/`,
        // target: `http://10.10.102.7:3333/`,
        // target: `http://localhost:3333`,
        // target: `http://20.242.144.95:8888/`,
        // target: `http://52.170.211.188:8888/`,
        // target: `http://172.210.26.95:9803/prod-api/`,
        //target: `http://20.127.204.47:8001/prod-api/`,
        //target: `http://52.170.211.188:8877`,
        //target: `http://43.137.4.139:9988`,
        target: `http://175.27.239.33:9988`,
        // target: `https://www.ny-zodiac.com/prod-api/`,
        // target: `http://10.10.101.120:8888/`,
        changeOrigin: true,
        pathRewrite: {
          ['^' + process.env.VUE_APP_BASE_API]: ''
        }
      },
      [process.env. VUE_APP_HOT_URL]: {
        target: `http://localhost:8888/gpt/hotQuestion/`,
        changeOrigin: true,
        pathRewrite: {
          ['^' + process.env. VUE_APP_HOT_URL]: ''
        }
      },
      [process.env.VUE_APP_FILE_URL]: {
        target: `https://nygpt.blob.core.windows.net/`,
        changeOrigin: true,
        pathRewrite: {
          ['^' + process.env. VUE_APP_FILE_URL]: ''
        }
      },
      [process.env.VUE_APP_SQL_API]: {
        // target: `http://118.89.203.164:5002/`,
        target:'https://inesa-test.synconize.com/sqlUrl',
        changeOrigin: true,
        pathRewrite: {
          ['^' + process.env.VUE_APP_SQL_API]: ''
        }
      },
      ['/cosUrl']:{
        // target: `http://118.89.203.164:5002/`,
        target:'https://deepseek-1340465638.cos.ap-nanjing.myqcloud.com/',
        changeOrigin: true,
        pathRewrite: {
          ['^' + '/cosUrl']: ''
        }
      },
      '/websocket': {
        // target: `wss://gptcs.yjbtest.com:8088/`,
        // target: `http://10.10.100.250:8889/`,
        // target: `wss://gptcs.yjbtest.com:8088/prod-api/`,
        // target: `http://10.10.101.15:8889/`,
        // target: `http://10.10.104.3:8889/`,
        target: `ws://10.10.102.187:8888/`,
        ws: true,
        changeOrigin: true,
      },
    },
    disableHostCheck: true
  },
  css: {
    loaderOptions: {
      sass: {
        sassOptions: { outputStyle: "expanded" }
      }
    }
  },
  configureWebpack: config => {
    config.module.rules.filter(rule => {
      return rule.test.toString().indexOf('scss') !== -1;
    })
      .forEach(rule => {
        rule.oneOf.forEach(oneOfRule => {
          oneOfRule.use.splice(oneOfRule.use.indexOf(require.resolve('sass-loader')), 0,
            { loader: require.resolve('css-unicode-loader') });
        });
      });
    return {
      name: name,
      resolve: {
        alias: {
          '@': resolve('src')
        }
      },
      plugins: [
        new CompressionPlugin({
          cache: false, // 不启用文件缓存
          test: /\.(js|css|html)?$/i, // 压缩文件格式
          filename: '[path].gz[query]', // 压缩后的文件名
          algorithm: 'gzip', // 使用gzip压缩
          minRatio: 0.8 // 压缩率小于1才会压缩
        }),
      ],
    };
  },
  chainWebpack (config) {
    config.plugins.delete('preload') // TODO: need test
    config.plugins.delete('prefetch') // TODO: need test

    // set svg-sprite-loader
    config.module
      .rule('svg')
      .exclude.add(resolve('src/assets/icons'))
      .end()
    config.module
      .rule('icons')
      .test(/\.svg$/)
      .include.add(resolve('src/assets/icons'))
      .end()
      .use('svg-sprite-loader')
      .loader('svg-sprite-loader')
      .options({
        symbolId: 'icon-[name]'
      })
      .end()

    config
      .when(process.env.NODE_ENV !== 'development',
        config => {
          config
            .plugin('ScriptExtHtmlWebpackPlugin')
            .after('html')
            .use('script-ext-html-webpack-plugin', [{
              // `runtime` must same as runtimeChunk name. default is `runtime`
              inline: /runtime\..*\.js$/
            }])
            .end()
          config
            .optimization.splitChunks({
              chunks: 'all',
              cacheGroups: {
                libs: {
                  name: 'chunk-libs',
                  test: /[\\/]node_modules[\\/]/,
                  priority: 10,
                  chunks: 'initial' // only package third parties that are initially dependent
                },
                elementUI: {
                  name: 'chunk-elementUI', // split elementUI into a single package
                  priority: 20, // the weight needs to be larger than libs and app or it will be packaged into libs or app
                  test: /[\\/]node_modules[\\/]_?element-ui(.*)/ // in order to adapt to cnpm
                },
                commons: {
                  name: 'chunk-commons',
                  test: resolve('src/components'), // can customize your rules
                  minChunks: 3, //  minimum common number
                  priority: 5,
                  reuseExistingChunk: true
                }
              }
            })
          config.optimization.runtimeChunk('single'),
          {
            from: path.resolve(__dirname, './public/robots.txt'), //防爬虫文件
            to: './' //到根目录下
          }
        }
      )
  }
}
