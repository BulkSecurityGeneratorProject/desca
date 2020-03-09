/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { DescaTestModule } from '../../../test.module';
import { InternationalStandardDetailComponent } from '../../../../../../main/webapp/app/entities/international-standard/international-standard-detail.component';
import { InternationalStandardService } from '../../../../../../main/webapp/app/entities/international-standard/international-standard.service';
import { InternationalStandard } from '../../../../../../main/webapp/app/entities/international-standard/international-standard.model';

describe('Component Tests', () => {

    describe('InternationalStandard Management Detail Component', () => {
        let comp: InternationalStandardDetailComponent;
        let fixture: ComponentFixture<InternationalStandardDetailComponent>;
        let service: InternationalStandardService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DescaTestModule],
                declarations: [InternationalStandardDetailComponent],
                providers: [
                    InternationalStandardService
                ]
            })
            .overrideTemplate(InternationalStandardDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(InternationalStandardDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(InternationalStandardService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new InternationalStandard(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.internationalStandard).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
