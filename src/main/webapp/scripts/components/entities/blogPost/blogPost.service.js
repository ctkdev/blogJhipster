'use strict';

angular.module('blogjhipsterApp')
    .factory('BlogPost', function ($resource, DateUtils) {
        return $resource('api/blogPosts/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.postDate = DateUtils.convertDateTimeFromServer(data.postDate);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
