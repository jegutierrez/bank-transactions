package ar.com.olx.entity;

/**
 * Created by jegutierrez on 09/10/16.
 */
public enum TransactionStatus {
    SUCCESS,
    FAILURE_SAME_ACCOUNT,
    FAILURE_ACCOUNT_NOT_FOUND,
    FAILURE_INSUFFICIENT_BALANCE
}