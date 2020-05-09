package xyz.zelda.company.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.validation.annotation.Validated
import xyz.zelda.company.CompanyConstant
import xyz.zelda.company.dto.*
import xyz.zelda.infra.api.BaseResponse
import xyz.zelda.infra.auth.AuthConstant
import xyz.zelda.infra.validation.Group1
import xyz.zelda.infra.validation.Group2

@FeignClient(name = CompanyConstant.SERVICE_NAME, path = "/v1/company", url = "\${zelda.company-service-endpoint}")
interface CompanyClient {
    // Company Apis
    @PostMapping(path = "/create")
    fun createCompany(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) authz: String?, @RequestBody @Validated([Group2::class]) companyDto: CompanyDto?): GenericCompanyResponse?

    @GetMapping(path = "/list")
    fun listCompanies(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) authz: String?, @RequestParam offset: Int, @RequestParam limit: Int): ListCompanyResponse?

    @GetMapping(path = "/get")
    fun getCompany(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) authz: String?, @RequestParam("company_id") companyId: String?): GenericCompanyResponse?

    @PutMapping(path = "/update")
    fun updateCompany(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) authz: String?, @RequestBody @Validated([Group1::class]) companyDto: CompanyDto?): GenericCompanyResponse?

    // Admin Apis
    @GetMapping(path = "/admin/list")
    fun listAdmins(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) authz: String?, @RequestParam companyId: String?): ListAdminResponse?

    @GetMapping(path = "/admin/get")
    fun getAdmin(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) authz: String?, @RequestParam companyId: String?, @RequestParam userId: String?): GenericDirectoryResponse?

    @PostMapping(path = "/admin/create")
    fun createAdmin(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) authz: String?, @RequestBody @Validated request: DirectoryEntryRequest?): GenericDirectoryResponse?

    @DeleteMapping(path = "/admin/delete")
    fun deleteAdmin(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) authz: String?, @RequestBody @Validated request: DirectoryEntryRequest?): BaseResponse?

    @GetMapping(path = "/admin/admin_of")
    fun getAdminOf(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) authz: String?, @RequestParam userId: String?): GetAdminOfResponse?

    // Directory Apis
    @PostMapping(path = "/directory/create")
    fun createDirectory(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) authz: String?, @RequestBody @Validated request: NewDirectoryEntry?): GenericDirectoryResponse?

    @GetMapping(path = "/directory/list")
    fun listDirectories(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) authz: String?, @RequestParam companyId: String?, @RequestParam offset: Int, @RequestParam limit: Int): ListDirectoryResponse?

    @GetMapping(path = "/directory/get")
    fun getDirectoryEntry(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) authz: String?, @RequestParam companyId: String?, @RequestParam userId: String?): GenericDirectoryResponse?

    @PutMapping(path = "/directory/update")
    fun updateDirectoryEntry(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) authz: String?, @RequestBody @Validated request: DirectoryEntryDto?): GenericDirectoryResponse?

    @GetMapping(path = "/directory/get_associations")
    fun getAssociations(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) authz: String?, @RequestParam companyId: String?, @RequestParam offset: Int, @RequestParam limit: Int): GetAssociationResponse?

    // WorkerDto Apis
    @GetMapping(path = "/worker/list")
    fun listWorkers(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) authz: String?, @RequestParam companyId: String?, @RequestParam teamId: String?): ListWorkerResponse?

    @GetMapping(path = "/worker/get")
    fun getWorker(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) authz: String?, @RequestParam companyId: String?, @RequestParam teamId: String?, @RequestParam userId: String?): GenericDirectoryResponse?

    @DeleteMapping(path = "/worker/delete")
    fun deleteWorker(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) authz: String?, @RequestBody @Validated workerDto: WorkerDto?): BaseResponse?

    @GetMapping(path = "/worker/get_worker_of")
    fun getWorkerOf(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) authz: String?, @RequestParam userId: String?): GetWorkerOfResponse?

    @PostMapping(path = "/worker/create")
    fun createWorker(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) authz: String?, @RequestBody @Validated workerDto: WorkerDto?): GenericDirectoryResponse?

    // Team Apis
    @PostMapping(path = "/team/create")
    fun createTeam(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) authz: String?, @RequestBody @Validated request: CreateTeamRequest?): GenericTeamResponse?

    @GetMapping(path = "/team/list")
    fun listTeams(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) authz: String?, @RequestParam companyId: String?): ListTeamResponse?

    @GetMapping(path = "/team/get")
    fun getTeam(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) authz: String?, @RequestParam companyId: String?, @RequestParam teamId: String?): GenericTeamResponse?

    @PutMapping(path = "/team/update")
    fun updateTeam(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) authz: String?, @RequestBody @Validated teamDto: TeamDto?): GenericTeamResponse?

    @GetMapping(path = "/team/get_worker_team_info")
    fun getWorkerTeamInfo(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) authz: String?, @RequestParam(required = false) companyId: String?, @RequestParam userId: String?): GenericWorkerResponse?

    // Job Apis
    @PostMapping(path = "/job/create")
    fun createJob(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) authz: String?, @RequestBody @Validated request: CreateJobRequest?): GenericJobResponse?

    @GetMapping(path = "/job/list")
    fun listJobs(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) authz: String?, @RequestParam companyId: String?, @RequestParam teamId: String?): ListJobResponse?

    @GetMapping(path = "/job/get")
    fun getJob(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) authz: String?, @RequestParam jobId: String?, @RequestParam companyId: String?, @RequestParam teamId: String?): GenericJobResponse?

    @PutMapping(path = "/job/update")
    fun updateJob(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) authz: String?, @RequestBody @Validated jobDto: JobDto?): GenericJobResponse?

    // Shift Apis
    @PostMapping(path = "/shift/create")
    fun createShift(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) authz: String?, @RequestBody @Validated request: CreateShiftRequest?): GenericShiftResponse?

    @PostMapping(path = "/shift/list_worker_shifts")
    fun listWorkerShifts(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) authz: String?, @RequestBody @Validated request: WorkerShiftListRequest?): GenericShiftListResponse?

    @PostMapping(path = "/shift/list_shifts")
    fun listShifts(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) authz: String?, @RequestBody @Validated request: ShiftListRequest?): GenericShiftListResponse?

    @PostMapping(path = "/shift/bulk_publish")
    fun bulkPublishShifts(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) authz: String?, @RequestBody @Validated request: BulkPublishShiftsRequest?): GenericShiftListResponse?

    @GetMapping(path = "/shift/get")
    fun getShift(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) authz: String?, @RequestParam shiftId: String?, @RequestParam teamId: String?, @RequestParam companyId: String?): GenericShiftResponse?

    @PutMapping(path = "/shift/update")
    fun updateShift(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) authz: String?, @RequestBody @Validated shiftDto: ShiftDto?): GenericShiftResponse?

    @DeleteMapping(path = "/shift/delete")
    fun deleteShift(@RequestHeader(AuthConstant.AUTHORIZATION_HEADER) authz: String?, @RequestParam shiftId: String?, @RequestParam teamId: String?, @RequestParam companyId: String?): BaseResponse?
}