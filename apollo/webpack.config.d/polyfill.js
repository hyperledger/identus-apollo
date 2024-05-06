config.resolve = {
    fallback: {
        stream: require.resolve("stream-browserify"),
        "url": require.resolve("url/")
    }
};
var webpack = require('webpack');

config.plugins.push(new webpack.ProvidePlugin({
    Buffer: ['buffer', 'Buffer'],
}));
