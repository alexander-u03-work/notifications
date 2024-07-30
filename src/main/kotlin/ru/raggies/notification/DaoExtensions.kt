package ru.raggies.notification

import org.jooq.UpdatableRecord
import org.jooq.impl.DAOImpl

fun <R: UpdatableRecord<R>, P, T> DAOImpl<R, P, T>.create(entity: P): P =
    this.ctx()
        .insertInto(this.table)
        .set(this.ctx().newRecord(this.table, entity))
        .returning()
        .fetchOne(this.mapper())!!

fun <R: UpdatableRecord<R>, P, T> DAOImpl<R, P, T>.createAll(entities: List<P>) {
    val records = entities.map {
        entity -> this.ctx().newRecord(this.table, entity)
    }
    this.ctx().batchInsert(records).execute()
}
