package uz.pdp.inventorymanagementsystem.utils

object CodeGenerator {

    fun forWarehouse(lastNumber: Int, prefix: String = "WH", digits: Int = 3): String {
        return "$prefix-%0${digits}d".format(lastNumber + 1)
    }

    fun forEmployee(lastNumber: Int, prefix: String = "EMP", digits: Int = 4): String {
        return "$prefix-%0${digits}d".format(lastNumber + 1)
    }

    fun forProduct(lastNumber: Int, prefix: String = "PRD", digits: Int = 5): String {
        return "$prefix-%0${digits}d".format(lastNumber + 1)
    }
}
