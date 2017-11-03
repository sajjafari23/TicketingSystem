(function() {
    'use strict';
    angular
        .module('ticketingSystemApp')
        .factory('TaskType', TaskType);

    TaskType.$inject = ['$resource'];

    function TaskType ($resource) {
        var resourceUrl =  'api/task-types/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
