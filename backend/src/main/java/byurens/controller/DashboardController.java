package byurens.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import byurens.dto.DashboardResponse;
import byurens.service.DashboardService;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor 
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/today")
    public ResponseEntity<DashboardResponse> getTodayDashboard() {
        return ResponseEntity.ok(dashboardService.getTodayDashboardData());
    }
}
