(function() {
    'use strict';

    angular
        .module('ticketingSystemApp')
        .controller('RequestDetailController', RequestDetailController);

    RequestDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Request', 'Priority', 'RequestType'];

    function RequestDetailController($scope, $rootScope, $stateParams, previousState, entity, Request, Priority, RequestType) {
        var vm = this;

        vm.request = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('ticketingSystemApp:requestUpdate', function(event, result) {
            vm.request = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
