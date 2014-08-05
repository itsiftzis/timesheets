class CreateProjectCtrl

    constructor: (@$log, @$location,  @ProjecService) ->
        @$log.debug "constructing CreateProjectController"
        @project = {}

    createProject: () ->
        @$log.debug "createProject()"
        @ProjectService.createProject(@project)
        .then(
            (data) =>
                @$log.debug "Promise returned #{data} Project"
                @project = data
                @$location.path("/")
            ,
            (error) =>
                @$log.error "Unable to create Project: #{error}"
            )

controllersModule3.controller('CreateProjectCtrl', CreateProjectCtrl)