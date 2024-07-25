package ru.danya02.simpleipcheck

class NetInterface(
    val name: String,
    val address: String
) {
    override fun toString(): String {
        return "$name: $address"
    }
}
