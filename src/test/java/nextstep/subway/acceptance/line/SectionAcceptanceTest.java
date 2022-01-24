package nextstep.subway.acceptance.line;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTest;
import nextstep.subway.acceptance.station.StationStep;
import nextstep.subway.common.exception.ErrorMessage;
import nextstep.subway.line.domain.dto.SectionRequest;
import nextstep.subway.utils.AcceptanceTestThen;
import nextstep.subway.utils.AcceptanceTestWhen;

@DisplayName("지하철 구간 관리")
public class SectionAcceptanceTest extends AcceptanceTest {
    private LineStep lineStep;
    private StationStep stationStep;
    private SectionStep sectionStep;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        stationStep = new StationStep();
        lineStep = new LineStep(stationStep);
        sectionStep = new SectionStep(lineStep);
    }

    /**
     * Given 지하철 노선이 존재하고
     * And   새로운 지하철역을 등록하고
     * When  구간 등록을 요청한다.
     * Then  구간 등록이 성공한다.
     */
    @DisplayName("구간 등록")
    @Test
    void addSection() {
        // given
        lineStep.지하철_노선_생성_요청();
        stationStep.지하철역_생성_요청();

        // when
        ExtractableResponse<Response> response = sectionStep.지하철_구간_생성_요청();

        // then
        AcceptanceTestThen.fromWhen(response)
                          .equalsHttpStatus(HttpStatus.CREATED)
                          .hasLocation();
    }

    /**
     * Given 지하철 노선이 존재하고
     * And   새로운 지하철역을 등록하고
     * When  등록할 구간의 상행역을 등록되어있는 구간의 하행역이 아니도록 구간 등록을 요청한다.
     * Then  구간 등록은 실패한다.
     */
    @DisplayName("구간 등록 실패 - 등록할 구간의 상행역이 등록되어있는 구간의 하행역이 아닐때")
    @Test
    void addSectionThatFailing1() {
        // given
        SectionRequest request = sectionStep.dummyRequest();
        long upStationInFirstSection = 1;
        request.setUpStationId(upStationInFirstSection);
        long downStationInNewSection = 3;
        request.setDownStationId(downStationInNewSection);

        // when
        ExtractableResponse<Response> response = sectionStep.지하철_구간_생성_요청(request);

        // then
        AcceptanceTestThen.fromWhen(response)
                          .equalsHttpStatus(HttpStatus.BAD_REQUEST)
                          .equalsErrorMessage(ErrorMessage.NOT_FOUND_SECTION_DOCKING_POINT.getMessage())
                          .hasNotLocation();
    }

    /**
     * Given 지하철 노선이 존재하고
     * And   새로운 지하철역을 등록하고
     * When  등록할 구간의 하행역을 구간에 이미 등록되있는 역으로 구간 등록을 요청한다.
     * Then  구간 등록은 실패한다.
     */
    @DisplayName("구간 등록 실패 - 등록할 구간의 하행역이 구간에 이미 등록되있는 역일때 ")
    @Test
    void addSectionThatFailing2() {
        // given
        SectionRequest request = sectionStep.dummyRequest();
        long downStationInFirstSection = 2;
        request.setUpStationId(downStationInFirstSection);
        long upStationInFirstSection = 1;
        request.setDownStationId(upStationInFirstSection);

        // when
        ExtractableResponse<Response> response = sectionStep.지하철_구간_생성_요청(request);

        // then
        AcceptanceTestThen.fromWhen(response)
                          .equalsHttpStatus(HttpStatus.BAD_REQUEST)
                          .equalsErrorMessage(ErrorMessage.ALREADY_REGISTERED_STATION_IN_SECTION.getMessage())
                          .hasNotLocation();
    }

    /**
     * Given 지하철 구간이 존재하고
     * When  구간 삭제를 요청한다.
     * Then  구간 삭제는 성공한다.
     */
    @DisplayName("구간 삭제")
    @Test
    void deleteSection() {
        // given
        ExtractableResponse<Response> createResponse = sectionStep.지하철_구간_생성_요청();

        // when
        AcceptanceTestWhen when = AcceptanceTestWhen.fromGiven(createResponse);
        ExtractableResponse<Response> deleteResponse = when.requestLocation(Method.DELETE);

        // then
        AcceptanceTestThen.fromWhen(deleteResponse)
                          .equalsHttpStatus(HttpStatus.NO_CONTENT);
    }

    /**
     * Given 지하철 구간이 존재하고
     * When  종점이 아닌 구간 삭제를 요청한다.
     * Then  구간 삭제는 실패한다.
     */
    @DisplayName("구간 삭제 실패 - 삭제할 구간이 종점이 아닐때")
    @Test
    void deleteSectionThatFailing1() {
        // given
        ExtractableResponse<Response> preCreateResponse = sectionStep.지하철_구간_생성_요청();
        sectionStep.지하철_구간_생성_요청();

        // when
        ExtractableResponse<Response> deleteResponse = AcceptanceTestWhen.fromGiven(preCreateResponse)
                                                    .requestLocation(Method.DELETE);

        // then
        AcceptanceTestThen.fromWhen(deleteResponse)
                          .equalsHttpStatus(HttpStatus.BAD_REQUEST)
                          .equalsErrorMessage("종점역의 구간만 삭제할 수 있습니다.");
    }

    /**
     * Given 지하철 구간이 존재하고
     * When  구간이 1개만 있을때 삭제를 요청한다.
     * Then  구간 삭제는 실패한다.
     */
    @DisplayName("구간 삭제 실패 - 구간이 1개만 있을때")
    @Test
    void deleteSectionThatFailing2() {
        // given
        lineStep.지하철_노선_생성_요청();

        // when
        ExtractableResponse<Response> response = RestAssured.given().log() .all()
                                                            .when()
                                                            .delete("/lines/1/sections/1")
                                                            .then().log().all()
                                                            .extract();

        // then
        AcceptanceTestThen.fromWhen(response)
                          .equalsHttpStatus(HttpStatus.BAD_REQUEST)
                          .equalsErrorMessage(ErrorMessage.BELOW_MIN_SECTION_SIZE.getMessage());
    }
}
