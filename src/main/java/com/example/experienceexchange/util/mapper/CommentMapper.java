package com.example.experienceexchange.util.mapper;

import com.example.experienceexchange.dto.CommentDto;
import com.example.experienceexchange.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentDto commentToCommentDto(Comment comment);

    @Mapping(target = "author", ignore = true)
    Comment commentDtoToComment(CommentDto commentDto);
}
