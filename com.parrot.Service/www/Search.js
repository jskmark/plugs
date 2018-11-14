module.exports = {
    searchKeyword: function (name, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "Search", "search", [name]);
    }
};
