/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { DescaTestModule } from '../../../test.module';
import { ApplicantDetailComponent } from '../../../../../../main/webapp/app/entities/applicant/applicant-detail.component';
import { ApplicantService } from '../../../../../../main/webapp/app/entities/applicant/applicant.service';
import { Applicant } from '../../../../../../main/webapp/app/entities/applicant/applicant.model';

describe('Component Tests', () => {

    describe('Applicant Management Detail Component', () => {
        let comp: ApplicantDetailComponent;
        let fixture: ComponentFixture<ApplicantDetailComponent>;
        let service: ApplicantService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DescaTestModule],
                declarations: [ApplicantDetailComponent],
                providers: [
                    ApplicantService
                ]
            })
            .overrideTemplate(ApplicantDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ApplicantDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ApplicantService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Applicant(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.applicant).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
