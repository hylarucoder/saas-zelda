package xyz.zelda.whoami.dto

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import xyz.zelda.company.dto.AdminOfList
import xyz.zelda.company.dto.WorkerOfList

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class IAmDto {
    private val support = false
    private val userId: String? = null
    private val workerOfList: WorkerOfList? = null
    private val adminOfList: AdminOfList? = null
}