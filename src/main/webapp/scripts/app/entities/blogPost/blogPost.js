'use strict';

angular.module('blogjhipsterApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('blogPost', {
                parent: 'entity',
                url: '/blogPost',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'blogjhipsterApp.blogPost.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/blogPost/blogPosts.html',
                        controller: 'BlogPostController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('blogPost');
                        return $translate.refresh();
                    }]
                }
            })
            .state('blogPostDetail', {
                parent: 'entity',
                url: '/blogPost/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'blogjhipsterApp.blogPost.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/blogPost/blogPost-detail.html',
                        controller: 'BlogPostDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('blogPost');
                        return $translate.refresh();
                    }]
                }
            });
    });
