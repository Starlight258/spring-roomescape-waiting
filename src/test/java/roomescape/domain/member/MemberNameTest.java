package roomescape.domain.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.exception.BadRequestException;

class MemberNameTest {

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void 멤버_이름은_비어있어서는_안된다(String input) {
        Assertions.assertThatThrownBy(() -> new MemberName(input))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Member name is mandatory");
    }

}
