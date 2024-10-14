package net.brightroom.postalcode.presentation.api

import am.ik.yavi.core.ConstraintViolations

interface Request {
    fun validate(): ConstraintViolations
}
