package uz.pdp.inventorymanagementsystem.config

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import uz.pdp.inventorymanagementsystem.repo.EmployeeRepo

@Service
class CustomUserDetailsService(
    private val employeeRepo: EmployeeRepo
) : UserDetailsService {

    override fun loadUserByUsername(phone: String): UserDetails {
        val employee = employeeRepo.findByPhone(phone)
            ?: throw UsernameNotFoundException("Employee not found with phone: $phone")

        return CustomUserDetails(employee)
    }
}
