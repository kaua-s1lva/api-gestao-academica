package br.ufes.ccens.common.util;

import java.time.LocalDate;

public interface IConverterDataFormat {
    LocalDate parse(String date);
}
