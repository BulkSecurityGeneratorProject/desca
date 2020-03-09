/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { DescaTestModule } from '../../../test.module';
import { DescaWayDetailComponent } from '../../../../../../main/webapp/app/entities/desca-way/desca-way-detail.component';
import { DescaWayService } from '../../../../../../main/webapp/app/entities/desca-way/desca-way.service';
import { DescaWay } from '../../../../../../main/webapp/app/entities/desca-way/desca-way.model';

describe('Component Tests', () => {

    describe('DescaWay Management Detail Component', () => {
        let comp: DescaWayDetailComponent;
        let fixture: ComponentFixture<DescaWayDetailComponent>;
        let service: DescaWayService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DescaTestModule],
                declarations: [DescaWayDetailComponent],
                providers: [
                    DescaWayService
                ]
            })
            .overrideTemplate(DescaWayDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(DescaWayDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DescaWayService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new DescaWay(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.descaWay).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
