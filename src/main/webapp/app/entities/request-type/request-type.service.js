(function() {
    'use strict';
    angular
        .module('ticketingSystemApp')
        .factory('RequestType', RequestType);

    RequestType.$inject = ['$resource'];

    function RequestType ($resource) {
        var resourceUrl =  'api/request-types/:id';

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
