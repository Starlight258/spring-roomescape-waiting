package roomescape.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.auth.RequireRole;
import roomescape.domain.member.MemberRole;

@Controller
@RequestMapping("/admin")
@RequireRole(role = MemberRole.ADMIN)
public class AdminViewController {

    @GetMapping
    public String getAdminHomePage() {
        return "admin/index";
    }

    @GetMapping("/reservation")
    public String getAdminReservationPage() {
        return "admin/reservation-new";
    }

    @GetMapping("/time")
    public String getAdminReservationTimePage() {
        return "admin/time";
    }

    @GetMapping("/theme")
    public String getAdminThemePage() {
        return "admin/theme";
    }

    @GetMapping("/waiting")
    public String getAdminWaitingPage() {
        return "admin/waiting";
    }
}
