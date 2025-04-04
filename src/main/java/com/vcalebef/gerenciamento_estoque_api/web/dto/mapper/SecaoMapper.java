package com.vcalebef.gerenciamento_estoque_api.web.dto.mapper;

import com.vcalebef.gerenciamento_estoque_api.entity.Secao;
import com.vcalebef.gerenciamento_estoque_api.web.dto.SecaoResponseDto;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class SecaoMapper {

    public static Secao toSecao(SecaoResponseDto responseDto) {
        return new ModelMapper().map(responseDto, Secao.class);
    }

    public static SecaoResponseDto toSecaoResponseDto(Secao secao) {
        return new ModelMapper().map(secao, SecaoResponseDto.class);
    }

    public static List<SecaoResponseDto> toListSecaoResponseDto(List<Secao> secoes) {

        return secoes.stream().map(secao -> toSecaoResponseDto(secao)).collect(Collectors.toList());

    }

}
