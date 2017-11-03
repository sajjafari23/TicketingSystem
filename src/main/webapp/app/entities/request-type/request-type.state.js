(function() {
    'use strict';

    angular
        .module('ticketingSystemApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('request-type', {
            parent: 'entity',
            url: '/request-type?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ticketingSystemApp.requestType.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/request-type/request-types.html',
                    controller: 'RequestTypeController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('requestType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('request-type-detail', {
            parent: 'request-type',
            url: '/request-type/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ticketingSystemApp.requestType.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/request-type/request-type-detail.html',
                    controller: 'RequestTypeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('requestType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'RequestType', function($stateParams, RequestType) {
                    return RequestType.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'request-type',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('request-type-detail.edit', {
            parent: 'request-type-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/request-type/request-type-dialog.html',
                    controller: 'RequestTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RequestType', function(RequestType) {
                            return RequestType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('request-type.new', {
            parent: 'request-type',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/request-type/request-type-dialog.html',
                    controller: 'RequestTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                code: null,
                                title: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('request-type', null, { reload: 'request-type' });
                }, function() {
                    $state.go('request-type');
                });
            }]
        })
        .state('request-type.edit', {
            parent: 'request-type',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/request-type/request-type-dialog.html',
                    controller: 'RequestTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RequestType', function(RequestType) {
                            return RequestType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('request-type', null, { reload: 'request-type' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('request-type.delete', {
            parent: 'request-type',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/request-type/request-type-delete-dialog.html',
                    controller: 'RequestTypeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['RequestType', function(RequestType) {
                            return RequestType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('request-type', null, { reload: 'request-type' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
