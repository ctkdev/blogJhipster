'use strict';

angular.module('blogjhipsterApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
