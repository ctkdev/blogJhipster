'use strict';

angular.module('blogjhipsterApp')
    .controller('BlogPostController', function ($scope, BlogPost, ParseLinks) {
        $scope.blogPosts = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            BlogPost.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.blogPosts = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            BlogPost.get({id: id}, function(result) {
                $scope.blogPost = result;
                $('#saveBlogPostModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.blogPost.id != null) {
                BlogPost.update($scope.blogPost,
                    function () {
                        $scope.refresh();
                    });
            } else {
                BlogPost.save($scope.blogPost,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            BlogPost.get({id: id}, function(result) {
                $scope.blogPost = result;
                $('#deleteBlogPostConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            BlogPost.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteBlogPostConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveBlogPostModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.blogPost = {title: null, content: null, user: null, excerpt: null, status: null, postType: null, postDate: null, commentsAllowed: null, guid: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
