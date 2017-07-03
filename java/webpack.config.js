const { resolve } = require('path');
const webpack = require('webpack');
const ExtractTextPlugin = require('extract-text-webpack-plugin');

module.exports = function(env) {

    var environment = env.mode;
    var isDevEnvironment = environment === 'development';

    var settings = {
        srcPath: './web_src/',
        distPath: resolve(__dirname, 'src/main/resources/public/dist') + '/',
        publicPath: '/dist/'
    };

    return {

        entry: [

            'babel-polyfill',
            'bootstrap-slider',
            'eonasdan-bootstrap-datetimepicker',
            settings.srcPath + 'js/app.js',
            settings.srcPath + 'scss/main.scss'

        ],

        devServer: {

            compress: true,
            historyApiFallback: true,
            contentBase: resolve(__dirname, 'public'),
            publicPath: '/' + settings.publicPath,
            port: 8080,
            host: 'localhost',

        },

        watchOptions: {

            ignored: '/node_modules/'

        },

        output: {

            path: settings.distPath,
            filename: 'js/[name].js',
            publicPath: settings.publicPath

        },

        module: {

            rules: [

                {

                    test: /\.js$/,
                    use: 'babel-loader',
                    exclude: /node_modules/

                },

                {

                    test: /\.scss$/,
                    use: ExtractTextPlugin.extract({
                        fallback: 'style-loader',
                        use: ['css-loader', 'sass-loader']
                    })

                },

                {

                    test: /\.css$/,
                    use: [ 'style-loader', 'css-loader', ],

                },

                // {
                //     test: /\.(png|jpg|jpeg|gif|svg|woff|woff2)$/,
                //     loader: 'url-loader?limit=10000',
                // },

                {
                    test: /\.(eot|ttf|woff|woff2|svg)$/,
                    loader: 'file-loader',
                }

            ],

        },

        resolve: {

            modules: [settings.srcPath, 'node_modules'],

        },

        plugins: isDevEnvironment ? [

            new ExtractTextPlugin('css/[name].css')

        ] : [

            new ExtractTextPlugin('css/[name].css'),

            new webpack.LoaderOptionsPlugin({
                minimize: true,
                debug: false
            }),

            new webpack.DefinePlugin({
                'process.env': {
                    'NODE_ENV': JSON.stringify('production')
                }
            }),

            new webpack.optimize.UglifyJsPlugin({
                comments: false,
                mangle: {
                    screw_ie8: true,
                },
                compress: {
                    screw_ie8: true,
                    warnings: false
                }
            })

        ],

    };

};
