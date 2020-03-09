/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DescaTestModule } from '../../../test.module';
import { MethodologyComponent } from '../../../../../../main/webapp/app/entities/methodology/methodology.component';
import { MethodologyService } from '../../../../../../main/webapp/app/entities/methodology/methodology.service';
import { Methodology } from '../../../../../../main/webapp/app/entities/methodology/methodology.model';

describe('Component Tests', () => {

    describe('Methodology Management Component', () => {
        let comp: MethodologyComponent;
        let fixture: ComponentFixture<MethodologyComponent>;
        let service: MethodologyService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DescaTestModule],
                declarations: [MethodologyComponent],
                providers: [
                    MethodologyService
                ]
            })
            .overrideTemplate(MethodologyComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MethodologyComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MethodologyService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Methodology(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.methodologies[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
