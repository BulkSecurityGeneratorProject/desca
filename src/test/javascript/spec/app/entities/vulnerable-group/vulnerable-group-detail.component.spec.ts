/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { DescaTestModule } from '../../../test.module';
import { VulnerableGroupDetailComponent } from '../../../../../../main/webapp/app/entities/vulnerable-group/vulnerable-group-detail.component';
import { VulnerableGroupService } from '../../../../../../main/webapp/app/entities/vulnerable-group/vulnerable-group.service';
import { VulnerableGroup } from '../../../../../../main/webapp/app/entities/vulnerable-group/vulnerable-group.model';

describe('Component Tests', () => {

    describe('VulnerableGroup Management Detail Component', () => {
        let comp: VulnerableGroupDetailComponent;
        let fixture: ComponentFixture<VulnerableGroupDetailComponent>;
        let service: VulnerableGroupService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DescaTestModule],
                declarations: [VulnerableGroupDetailComponent],
                providers: [
                    VulnerableGroupService
                ]
            })
            .overrideTemplate(VulnerableGroupDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(VulnerableGroupDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(VulnerableGroupService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new VulnerableGroup(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.vulnerableGroup).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
