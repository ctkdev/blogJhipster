'use strict';

angular.module('blogjhipsterApp')
    .controller('BlogPostDetailController', function ($scope, $stateParams, BlogPost) {
        $scope.blogPost = {};
        $scope.load = function (id) {
            BlogPost.get({id: id}, function(result) {
              $scope.blogPost = result;
            });
        };
        $scope.load($stateParams.id);
    });
