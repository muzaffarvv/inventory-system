package uz.pdp.inventorymanagementsystem.config

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import uz.pdp.inventorymanagementsystem.model.Employee
import java.util.*

class CustomUserDetails(
    private val employee: Employee
) : UserDetails {

    val id: UUID = employee.id!!
    val warehouseId: UUID = employee.warehouse.id!!

    override fun getUsername(): String = employee.phone

    override fun getPassword(): String = employee.password

    override fun getAuthorities(): Collection<GrantedAuthority> {
        val authorities = mutableSetOf<GrantedAuthority>()

        // ROLE lar
        employee.roles.forEach { role ->
            authorities.add(SimpleGrantedAuthority("ROLE_${role.code}"))

            // PERMISSION lar
            role.permissions.forEach { permission ->
                authorities.add(SimpleGrantedAuthority(permission.code))
            }
        }

        return authorities
    }

    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true
    override fun isCredentialsNonExpired() = true
    override fun isEnabled() = !employee.deleted
}
