#! /usr/bin/env ringo-web
// adapter to preview site via ringo

var response = require("ringo/jsgi/response");
var strings = require("ringo/utils/strings");
var subprocess = require("ringo/subprocess");

exports.app = function(req) {
    var path = req.pathInfo;
    if (strings.endsWith(path, "/")) path += "index.html";
    var resource = getResource("./_site" + path);
    if (resource.exists()) {
        if (strings.endsWith(path, ".html")) {
            subprocess.system("jekyll", {dir: module.directory});
        }
        return response.static(resource);
    }
    return response.notFound(req.pathInfo);
}
