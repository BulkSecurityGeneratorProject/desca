/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DescaTestModule } from '../../../test.module';
import { InternationalStandarComponent } from '../../../../../../main/webapp/app/entities/international-standar/international-standar.component';
import { InternationalStandarService } from '../../../../../../main/webapp/app/entities/international-standar/international-standar.service';
import { InternationalStandar } from '../../../../../../main/webapp/app/entities/international-standar/international-standar.model';

describe('Component Tests', () => {

    describe('InternationalStandar Management Component', () => {
        let comp: InternationalStandarComponent;
        let fixture: ComponentFixture<InternationalStandarComponent>;
        let service: InternationalStandarService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DescaTestModule],
                declarations: [InternationalStandarComponent],
                providers: [
                    InternationalStandarService
                ]
            })
            .overrideTemplate(InternationalStandarComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(InternationalStandarComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(InternationalStandarService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new InternationalStandar(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.internationalStandars[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
