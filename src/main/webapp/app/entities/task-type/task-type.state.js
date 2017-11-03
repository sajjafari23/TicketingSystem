(function() {
    'use strict';

    angular
        .module('ticketingSystemApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('task-type', {
            parent: 'entity',
            url: '/task-type?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ticketingSystemApp.taskType.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/task-type/task-types.html',
                    controller: 'TaskTypeController',
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
                    $translatePartialLoader.addPart('taskType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('task-type-detail', {
            parent: 'task-type',
            url: '/task-type/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ticketingSystemApp.taskType.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/task-type/task-type-detail.html',
                    controller: 'TaskTypeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('taskType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TaskType', function($stateParams, TaskType) {
                    return TaskType.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'task-type',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('task-type-detail.edit', {
            parent: 'task-type-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/task-type/task-type-dialog.html',
                    controller: 'TaskTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TaskType', function(TaskType) {
                            return TaskType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('task-type.new', {
            parent: 'task-type',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/task-type/task-type-dialog.html',
                    controller: 'TaskTypeDialogController',
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
                    $state.go('task-type', null, { reload: 'task-type' });
                }, function() {
                    $state.go('task-type');
                });
            }]
        })
        .state('task-type.edit', {
            parent: 'task-type',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/task-type/task-type-dialog.html',
                    controller: 'TaskTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TaskType', function(TaskType) {
                            return TaskType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('task-type', null, { reload: 'task-type' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('task-type.delete', {
            parent: 'task-type',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/task-type/task-type-delete-dialog.html',
                    controller: 'TaskTypeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TaskType', function(TaskType) {
                            return TaskType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('task-type', null, { reload: 'task-type' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
