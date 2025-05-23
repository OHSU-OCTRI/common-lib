const path = require('path');
const fs = require('fs');

const srcDir = './src/main/resources/static/assets/js';
const files = fs.readdirSync(srcDir).filter(file => file.endsWith('.js'));

const entries = {};
files.forEach(file => {
  const name = file.replace(/\.js$/, '');
  entries[name] = path.resolve(__dirname, srcDir, file);
});

module.exports = {
  entry: entries,
  output: {
    filename: '[name].js',
    path: path.resolve(__dirname, 'target/classes/static/assets/js'),
  },
  mode: 'production',
};
