package br.com.zippydeliveryapi.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashBoardResponse {

    private Integer totalSales;
    private Double totalRevenue;
    private Integer salesToday;
    private Double averageRevenue;

}
