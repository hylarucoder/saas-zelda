package xyz.zelda.company.service

import com.github.structlog4j.ILogger
import com.github.structlog4j.SLoggerFactory
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import xyz.zelda.company.dto.CompanyDto
import xyz.zelda.company.dto.CompanyList
import xyz.zelda.company.model.Company
import xyz.zelda.company.repo.CompanyRepo
import xyz.zelda.company.service.helper.ServiceHelper
import xyz.zelda.infra.api.ResultCode
import xyz.zelda.infra.auditlog.LogEntry
import xyz.zelda.infra.auth.AuthContext
import xyz.zelda.infra.exception.ServiceException
import java.util.stream.Collectors
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Service
class CompanyService {
    @Autowired
    private val companyRepo: CompanyRepo? = null

    @Autowired
    private val serviceHelper: ServiceHelper? = null

    @Autowired
    private val modelMapper: ModelMapper? = null

    @PersistenceContext
    private val entityManager: EntityManager? = null
    fun createCompany(companyDto: CompanyDto?): CompanyDto? {
        val company = convertToModel(companyDto)
        var savedCompany: Company? = null
        try {
            savedCompany = companyRepo.save(company)
        } catch (ex: Exception) {
            val errMsg = "could not create company"
            serviceHelper!!.handleErrorAndThrowException(logger, ex, errMsg)
        }
        val auditLog: LogEntry = LogEntry.builder()
                .currentUserId(AuthContext.getUserId())
                .authorization(AuthContext.getAuthz())
                .targetType("company")
                .targetId(company.getId())
                .companyId(company.getId())
                .teamId("")
                .updatedContents(company.toString())
                .build()
        logger.info("created company", auditLog)
        serviceHelper!!.trackEventAsync("company_created")
        return convertToDto(savedCompany)
    }

    fun listCompanies(offset: Int, limit: Int): CompanyList? {
        var limit = limit
        if (limit <= 0) {
            limit = 20
        }
        val pageRequest: Pageable = PageRequest.of(offset, limit)
        var companyPage: Page<Company?>? = null
        try {
            companyPage = companyRepo.findAll(pageRequest)
        } catch (ex: Exception) {
            val errMsg = "fail to query database for company list"
            serviceHelper!!.handleErrorAndThrowException(logger, ex, errMsg)
        }
        val companyDtoList: MutableList<CompanyDto?> = companyPage.getContent().stream().map({ company -> convertToDto(company) }).collect(Collectors.toList())
        return CompanyList.builder()
                .limit(limit)
                .offset(offset)
                .companies(companyDtoList)
                .build()
    }

    fun getCompany(companyId: String?): CompanyDto? {
        val company = companyRepo!!.findCompanyById(companyId)
                ?: throw ServiceException(ResultCode.NOT_FOUND, "Company not found")
        return convertToDto(company)
    }

    fun updateCompany(companyDto: CompanyDto?): CompanyDto? {
        val existingCompany = companyRepo!!.findCompanyById(companyDto.getId())
                ?: throw ServiceException(ResultCode.NOT_FOUND, "Company not found")
        entityManager.detach(existingCompany)
        val companyToUpdate = convertToModel(companyDto)
        var updatedCompany: Company? = null
        try {
            updatedCompany = companyRepo.save(companyToUpdate)
        } catch (ex: Exception) {
            val errMsg = "could not update the companyDto"
            serviceHelper!!.handleErrorAndThrowException(logger, ex, errMsg)
        }
        val auditLog: LogEntry = LogEntry.builder()
                .currentUserId(AuthContext.getUserId())
                .authorization(AuthContext.getAuthz())
                .targetType("company")
                .targetId(companyToUpdate.getId())
                .companyId(companyToUpdate.getId())
                .teamId("")
                .originalContents(existingCompany.toString())
                .updatedContents(updatedCompany.toString())
                .build()
        logger.info("updated company", auditLog)
        serviceHelper!!.trackEventAsync("company_updated")
        return convertToDto(updatedCompany)
    }

    private fun convertToDto(company: Company?): CompanyDto? {
        return modelMapper.map(company, CompanyDto::class.java)
    }

    private fun convertToModel(companyDto: CompanyDto?): Company? {
        return modelMapper.map(companyDto, Company::class.java)
    }

    companion object {
        val logger: ILogger? = SLoggerFactory.getLogger(CompanyService::class.java)
    }
}