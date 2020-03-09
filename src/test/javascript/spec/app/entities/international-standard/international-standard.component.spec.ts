/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DescaTestModule } from '../../../test.module';
import { InternationalStandardComponent } from '../../../../../../main/webapp/app/entities/international-standard/international-standard.component';
import { InternationalStandardService } from '../../../../../../main/webapp/app/entities/international-standard/international-standard.service';
import { InternationalStandard } from '../../../../../../main/webapp/app/entities/international-standard/international-standard.model';

describe('Component Tests', () => {

    describe('InternationalStandard Management Component', () => {
        let comp: InternationalStandardComponent;
        let fixture: ComponentFixture<InternationalStandardComponent>;
        let service: InternationalStandardService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DescaTestModule],
                declarations: [InternationalStandardComponent],
                providers: [
                    InternationalStandardService
                ]
            })
            .overrideTemplate(InternationalStandardComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(InternationalStandardComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(InternationalStandardService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new InternationalStandard(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.internationalStandards[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
