/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { DescaTestModule } from '../../../test.module';
import { RepairDetailComponent } from '../../../../../../main/webapp/app/entities/repair/repair-detail.component';
import { RepairService } from '../../../../../../main/webapp/app/entities/repair/repair.service';
import { Repair } from '../../../../../../main/webapp/app/entities/repair/repair.model';

describe('Component Tests', () => {

    describe('Repair Management Detail Component', () => {
        let comp: RepairDetailComponent;
        let fixture: ComponentFixture<RepairDetailComponent>;
        let service: RepairService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DescaTestModule],
                declarations: [RepairDetailComponent],
                providers: [
                    RepairService
                ]
            })
            .overrideTemplate(RepairDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(RepairDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RepairService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Repair(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.repair).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
