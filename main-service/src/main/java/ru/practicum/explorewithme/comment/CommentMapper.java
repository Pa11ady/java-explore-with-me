package ru.practicum.explorewithme.comment;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.category.dto.NewCategoryDto;
import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.comment.dto.CommentDto;
import ru.practicum.explorewithme.comment.dto.NewCommentDto;
import ru.practicum.explorewithme.comment.model.Comment;
import ru.practicum.explorewithme.event.EventMapper;
import ru.practicum.explorewithme.user.dto.UserShortDto;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentMapper {
    public static CommentDto mapToCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                new UserShortDto(comment.getAuthor().getId(), comment.getAuthor().getName()),
                comment.getCreated(),
                EventMapper.mapToEventShortDto(comment.getEvent()),
                comment.getStatus()
                );
    }

    public static List<CommentDto> mapToCommentDto(Iterable<Comment> comments) {
        List<CommentDto> result = new ArrayList<>();

        for (var el : comments) {
            result.add(mapToCommentDto(el));
        }
        return result;
    }
}
