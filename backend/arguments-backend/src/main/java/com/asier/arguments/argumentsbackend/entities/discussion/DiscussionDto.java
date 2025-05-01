package com.asier.arguments.argumentsbackend.entities.discussion;

import com.asier.arguments.argumentsbackend.utils.annotations.Mandatory;
import com.asier.arguments.argumentsbackend.utils.annotations.Modifiable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This DTO is for simplify the creation of discussions in client. The rest of the params will be assigned automatically
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class DiscussionDto {
    @Mandatory
    private String title;
    @Modifiable
    @Mandatory
    private Integer maxUsers;
    @Mandatory
    @Modifiable
    private Integer duration;
}
